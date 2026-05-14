from flask import Blueprint, request, jsonify
from flask_jwt_extended import create_access_token, jwt_required, get_jwt_identity
from app import db
from app.models import User
from app.utils import validate_email, validate_password
from datetime import datetime

auth_bp = Blueprint('auth', __name__)

@auth_bp.route('/register', methods=['POST'])
def register():
    """Register a new user"""
    data = request.get_json()
    
    # Validation
    if not data:
        return {'error': 'No data provided'}, 400
    
    username = data.get('username', '').strip()
    email = data.get('email', '').strip()
    password = data.get('password', '')
    first_name = data.get('first_name', '').strip()
    last_name = data.get('last_name', '').strip()
    
    # Validate inputs
    if not username or len(username) < 3:
        return {'error': 'Username must be at least 3 characters'}, 400
    
    if not validate_email(email):
        return {'error': 'Invalid email address'}, 400
    
    if not validate_password(password):
        return {'error': 'Password must be at least 8 characters'}, 400
    
    # Check if user exists
    if User.query.filter_by(username=username).first():
        return {'error': 'Username already exists'}, 409
    
    if User.query.filter_by(email=email).first():
        return {'error': 'Email already exists'}, 409
    
    # Create new user
    try:
        user = User(
            username=username,
            email=email,
            first_name=first_name,
            last_name=last_name
        )
        user.set_password(password)
        
        db.session.add(user)
        db.session.commit()
        
        # Create JWT token
        access_token = create_access_token(identity=user.id)
        
        return {
            'message': 'User registered successfully',
            'user': user.to_dict(),
            'access_token': access_token
        }, 201
    except Exception as e:
        db.session.rollback()
        return {'error': str(e)}, 500

@auth_bp.route('/login', methods=['POST'])
def login():
    """Login user"""
    data = request.get_json()
    
    if not data:
        return {'error': 'No data provided'}, 400
    
    username = data.get('username', '').strip()
    password = data.get('password', '')
    
    if not username or not password:
        return {'error': 'Username and password are required'}, 400
    
    # Find user
    user = User.query.filter_by(username=username).first()
    
    if not user:
        return {'error': 'Invalid credentials'}, 401
    
    # Check password
    if not user.check_password(password):
        return {'error': 'Invalid credentials'}, 401
    
    if not user.is_active:
        return {'error': 'User account is inactive'}, 403
    
    # Create JWT token
    access_token = create_access_token(identity=user.id)
    
    return {
        'message': 'Login successful',
        'user': user.to_dict(),
        'access_token': access_token
    }, 200

@auth_bp.route('/verify', methods=['GET'])
@jwt_required()
def verify():
    """Verify JWT token"""
    user_id = get_jwt_identity()
    user = User.query.get(user_id)
    
    if not user:
        return {'error': 'User not found'}, 404
    
    return {
        'valid': True,
        'user': user.to_dict()
    }, 200

@auth_bp.route('/refresh', methods=['POST'])
@jwt_required()
def refresh():
    """Refresh JWT token"""
    user_id = get_jwt_identity()
    user = User.query.get(user_id)
    
    if not user:
        return {'error': 'User not found'}, 404
    
    # Create new token
    new_token = create_access_token(identity=user.id)
    
    return {
        'message': 'Token refreshed',
        'access_token': new_token
    }, 200

@auth_bp.route('/users/<int:user_id>', methods=['GET'])
def get_user(user_id):
    """Get user by ID (public endpoint)"""
    user = User.query.get(user_id)
    
    if not user:
        return {'error': 'User not found'}, 404
    
    return user.to_dict(), 200
