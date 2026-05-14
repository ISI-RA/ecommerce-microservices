# E-commerce Microservices Backend

A complete digital transformation project showcasing **microservices architecture** with multiple programming languages, databases, cloud deployment, and CI/CD pipelines.

## 📋 Project Overview

This is a production-ready e-commerce backend built with:
- **Products & Orders APIs** - Node.js (Express)
- **User Authentication & JWT** - Python (Flask)
- **Payment & Notifications** - Java (Spring Boot)
- **Frontend** - React (for testing)
- **Database** - PostgreSQL
- **Cloud** - AWS (EC2/Elastic Beanstalk)
- **DevOps** - Docker, Docker Compose, GitHub Actions

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        React Frontend                        │
└────────┬────────────────────────────────────────────────────┘
         │
    ┌────┴──────────────────────────────────────┐
    │                                           │
┌───▼──────────────┐  ┌─────────────────┐  ┌──▼────────────────┐
│  Auth Service    │  │ Products Service │  │ Payment Service   │
│  (Python Flask)  │  │ (Node.js Express)│  │ (Java Spring Boot)│
│                  │  │                 │  │                   │
│ - Register       │  │ - Get Products  │  │ - Process Payment │
│ - Login          │  │ - Create Orders │  │ - Send Emails     │
│ - JWT Tokens     │  │ - Update Orders │  │ - Notifications   │
└──────────────────┘  └─────────────────┘  └───────────────────┘
         │                    │                      │
         └────────────┬───────┴──────────────────────┘
                      │
            ┌─────────▼──────────┐
            │   PostgreSQL DB    │
            │                    │
            │ - Users table      │
            │ - Products table   │
            │ - Orders table     │
            │ - Payments table   │
            └────────────────────┘
```

## 🚀 Quick Start

### Prerequisites
- Docker & Docker Compose
- Node.js 18+
- Python 3.9+
- Java 11+
- PostgreSQL 13+

### Option 1: Run Locally with Docker Compose

```bash
# Clone the repository
git clone https://github.com/ISI-RA/ecommerce-microservices.git
cd ecommerce-microservices

# Start all services
docker-compose up -d

# Services will be available at:
# - Products API: http://localhost:3001
# - Auth API: http://localhost:5000
# - Payment API: http://localhost:8080
# - React Frontend: http://localhost:3000
# - PostgreSQL: localhost:5432
```

### Option 2: Run Services Individually

#### Auth Service (Python)
```bash
cd auth-service
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
pip install -r requirements.txt
flask run --port=5000
```

#### Products Service (Node.js)
```bash
cd products-service
npm install
npm run dev
# Runs on port 3001
```

#### Payment Service (Java)
```bash
cd payment-service
mvn spring-boot:run
# Runs on port 8080
```

#### Frontend (React)
```bash
cd frontend
npm install
npm start
# Runs on port 3000
```

## 📚 API Documentation

### Auth Service (Python Flask)
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user
- `GET /api/auth/verify` - Verify JWT token
- `POST /api/auth/refresh` - Refresh token

### Products Service (Node.js Express)
- `GET /api/products` - Get all products
- `GET /api/products/:id` - Get product by ID
- `POST /api/products` - Create product (admin)
- `PUT /api/products/:id` - Update product (admin)
- `DELETE /api/products/:id` - Delete product (admin)
- `POST /api/orders` - Create order
- `GET /api/orders/:id` - Get order details
- `PUT /api/orders/:id` - Update order status

### Payment Service (Java Spring Boot)
- `POST /api/payments/process` - Process payment
- `GET /api/payments/:id` - Get payment status
- `POST /api/notifications/email` - Send email notification
- `POST /api/notifications/sms` - Send SMS notification

## 🗄️ Database Schema

See `docs/DATABASE_SCHEMA.sql` for complete schema.

**Main Tables:**
- `users` - User accounts and credentials
- `products` - Product catalog
- `orders` - Customer orders
- `order_items` - Items in each order
- `payments` - Payment records

## 🔐 Security Features

- JWT-based authentication
- Password hashing with bcrypt
- CORS protection
- Input validation
- SQL injection prevention
- Environment variable management

## 📦 Deployment

### AWS Elastic Beanstalk
```bash
# Deploy each service separately
cd products-service
eb init -p "Node.js 18 running on 64bit Amazon Linux 2" ecommerce-products
eb create ecommerce-products-env
eb deploy
```

### AWS EC2
See `docs/DEPLOYMENT.md` for step-by-step EC2 deployment guide.

## 🔄 CI/CD Pipeline

GitHub Actions automatically:
- Runs tests on every push
- Builds Docker images
- Pushes to Docker registry
- Deploys to AWS

See `.github/workflows/` for pipeline configuration.

## 📖 Documentation

- `docs/ARCHITECTURE.md` - System design and decision rationale
- `docs/API_DOCUMENTATION.md` - Detailed API specs
- `docs/DEPLOYMENT.md` - Deployment guide
- `docs/DATABASE_SCHEMA.sql` - Database design

## 🛠️ Tech Stack

| Service | Technology | Port |
|---------|-----------|------|
| Authentication | Python Flask, JWT | 5000 |
| Products & Orders | Node.js Express, PostgreSQL | 3001 |
| Payments & Notifications | Java Spring Boot | 8080 |
| Frontend | React, Axios | 3000 |
| Database | PostgreSQL | 5432 |
| Container | Docker, Docker Compose | - |
| CI/CD | GitHub Actions | - |

## 📊 Project Features

✅ Multi-language microservices architecture
✅ RESTful APIs with proper error handling
✅ JWT authentication across services
✅ Database transactions and consistency
✅ Docker containerization
✅ Docker Compose orchestration
✅ GitHub Actions CI/CD pipeline
✅ Comprehensive API documentation
✅ Database migrations
✅ Environment-based configuration
✅ Logging and monitoring ready
✅ AWS deployment ready

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👤 Author

**ISI-RA**
- GitHub: [@ISI-RA](https://github.com/ISI-RA)

## 📞 Support

For issues and questions, please open a GitHub issue or check the documentation in the `docs/` folder.

---

**Built with ❤️ for digital transformation and microservices learning**
