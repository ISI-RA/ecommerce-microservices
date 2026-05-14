const validateProduct = (req, res, next) => {
  const { name, price } = req.body;
  
  if (!name || name.trim().length === 0) {
    return res.status(400).json({ error: 'Product name is required' });
  }
  
  if (price === undefined || price === null || isNaN(price) || price <= 0) {
    return res.status(400).json({ error: 'Valid product price is required' });
  }
  
  next();
};

const validateOrder = (req, res, next) => {
  const { user_id, items, shipping_address } = req.body;
  
  if (!user_id) {
    return res.status(400).json({ error: 'user_id is required' });
  }
  
  if (!Array.isArray(items) || items.length === 0) {
    return res.status(400).json({ error: 'items array is required and must not be empty' });
  }
  
  for (const item of items) {
    if (!item.product_id || !item.quantity || item.quantity <= 0) {
      return res.status(400).json({ error: 'Each item must have product_id and quantity' });
    }
  }
  
  if (!shipping_address) {
    return res.status(400).json({ error: 'shipping_address is required' });
  }
  
  next();
};

module.exports = { validateProduct, validateOrder };
