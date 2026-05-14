const express = require('express');
const router = express.Router();
const { Order, OrderItem, Product } = require('../models');
const { validateOrder } = require('../middleware/validation');
const axios = require('axios');

// Create order
router.post('/', validateOrder, async (req, res, next) => {
  try {
    const { user_id, items, shipping_address } = req.body;

    // Calculate total and validate stock
    let total_amount = 0;
    for (const item of items) {
      const product = await Product.findByPk(item.product_id);
      if (!product) return res.status(404).json({ error: `Product ${item.product_id} not found` });
      if (product.stock < item.quantity) {
        return res.status(400).json({ error: `Insufficient stock for ${product.name}` });
      }
      total_amount += product.price * item.quantity;
    }

    // Create order
    const order = await Order.create({
      user_id,
      total_amount,
      shipping_address,
    });

    // Create order items
    for (const item of items) {
      const product = await Product.findByPk(item.product_id);
      await OrderItem.create({
        order_id: order.id,
        product_id: item.product_id,
        quantity: item.quantity,
        unit_price: product.price,
      });
      await product.update({ stock: product.stock - item.quantity });
    }

    // Fetch complete order
    const fullOrder = await Order.findByPk(order.id, { include: { association: 'items' } });
    res.status(201).json(fullOrder);
  } catch (error) {
    next(error);
  }
});

// Get order by ID
router.get('/:id', async (req, res, next) => {
  try {
    const order = await Order.findByPk(req.params.id, { 
      include: { 
        association: 'items',
        include: { association: 'product' }
      }
    });
    if (!order) return res.status(404).json({ error: 'Order not found' });
    res.json(order);
  } catch (error) {
    next(error);
  }
});

// Update order status
router.put('/:id', async (req, res, next) => {
  try {
    const { status, payment_status } = req.body;
    const order = await Order.findByPk(req.params.id);
    if (!order) return res.status(404).json({ error: 'Order not found' });

    if (status) order.status = status;
    if (payment_status) order.payment_status = payment_status;
    await order.save();

    res.json(order);
  } catch (error) {
    next(error);
  }
});

module.exports = router;
