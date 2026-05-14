from flask import Flask
from flask_cors import CORS
from flask_jwt_extended import JWTManager
from flask_sqlalchemy import SQLAlchemy
import os
from dotenv import load_dotenv

load_dotenv()

app = Flask(__name__)
CORS(app)

# Configuration
app.config['SQLALCHEMY_DATABASE_URI'] = os.getenv('DATABASE_URL')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['JWT_SECRET_KEY'] = os.getenv('JWT_SECRET_KEY', 'super-secret')
app.config['JWT_ACCESS_TOKEN_EXPIRES'] = 24 * 3600  # 24 hours

# Initialize extensions
db = SQLAlchemy(app)
jwt = JWTManager(app)

# Import models and routes
from app.models import User
from app.routes import auth_bp

app.register_blueprint(auth_bp, url_prefix='/api/auth')

# Error handlers
@app.errorhandler(404)
def not_found(error):
    return {'error': 'Not found'}, 404

@app.errorhandler(500)
def internal_error(error):
    return {'error': 'Internal server error'}, 500

@app.route('/health')
def health():
    return {'status': 'healthy', 'service': 'auth-service'}

if __name__ == '__main__':
    with app.app_context():
        db.create_all()
    app.run(host='0.0.0.0', port=5000, debug=os.getenv('FLASK_ENV') == 'development')
