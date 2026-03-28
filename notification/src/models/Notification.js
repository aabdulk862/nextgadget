const mongoose = require('mongoose');

const notificationSchema = new mongoose.Schema({
  userId:    { type: String, required: true, index: true },
  message:   { type: String, required: true },
  status:    { type: String, enum: ['unread', 'read'], default: 'unread' },
  type:      { type: String, default: 'ORDER_EVENT' },
  createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Notification', notificationSchema);
