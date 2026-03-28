const mongoose = require('mongoose');
const Notification = require('../models/Notification');

const getNotifications = async (req, res) => {
  try {
    const { userId } = req.params;
    if (!userId || !userId.trim()) {
      return res.status(400).json({ error: 'Invalid userId parameter' });
    }

    const notifications = await Notification.find({ userId })
      .sort({ createdAt: -1 });
    res.json(notifications);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

const getUnreadNotifications = async (req, res) => {
  try {
    const { userId } = req.params;
    if (!userId || !userId.trim()) {
      return res.status(400).json({ error: 'Invalid userId parameter' });
    }

    const notifications = await Notification.find({ userId, status: 'unread' })
      .sort({ createdAt: -1 });
    res.json(notifications);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

const markAsRead = async (req, res) => {
  try {
    const { id } = req.params;
    if (!mongoose.Types.ObjectId.isValid(id)) {
      return res.status(400).json({ error: 'Invalid notification id' });
    }

    const notification = await Notification.findByIdAndUpdate(
      id,
      { status: 'read' },
      { new: true }
    );

    if (!notification) {
      return res.status(404).json({ error: 'Notification not found' });
    }

    res.json(notification);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

module.exports = { getNotifications, getUnreadNotifications, markAsRead };
