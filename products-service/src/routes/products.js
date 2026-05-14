const express = require('express');
const router = express.Router();
const { Product } = require('../models');
const { validateProduct } = require('../middleware/validation');

// Get all products
router.get('/', async (req, res, next) => {
  try {
    const { category, min_price, max_price, search } = req.query;
    let where = { is_active: true };

    if (category) where.category = category;
    if (min_price || max_price) {
      where.price = {};
      if (min_price) where.price[require('sequelize').Op.gte] = min_price;
      if (max_price) where.price[require('sequelize').Op.lte] = max_price;
    }
    if (search) {
      where.name = { [require('sequelize').Op.iLike]: `%${search}%` };
    }

    const products = await Product.findAll({ where, limit: 100 });
    res.json({ count: products.length, data: products });
  } catch (error) {
    next(error);
  }
});

// Get product by ID
router.get('/:id', async (req, res, next) => {
  try {
    const product = await Product.findByPk(req.params.id);
    if (!product) return res.status(404).json({ error: 'Product not found' });
    res.json(product);
  } catch (error) {
    next(error);
  }
});

// Create product (protected)
router.post('/', validateProduct, async (req, res, next) => {
  try {
    const product = await Product.create(req.body);
    res.status(201).json(product);
  } catch (error) {
    next(error);
  }
});

// Update product (protected)
router.put('/:id', validateProduct, async (req, res, next) => {
  try {
    const product = await Product.findByPk(req.params.id);
    if (!product) return res.status(404).json({ error: 'Product not found' });
    
    await product.update(req.body);
    res.json(product);
  } catch (error) {
    next(error);
  }
});

// Delete product (protected)
router.delete('/:id', async (req, res, next) => {
  try {
    const product = await Product.findByPk(req.params.id);
    if (!product) return res.status(404).json({ error: 'Product not found' });
    
    await product.destroy();
    res.json({ message: 'Product deleted' });
  } catch (error) {
    next(error);
  }
});

module.exports = router;
