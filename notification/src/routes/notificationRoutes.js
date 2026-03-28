const express = require('express');
const router = express.Router();
const {
  getNotifications,
  getUnreadNotifications,
  markAsRead
} = require('../controllers/notificationController');

router.get('/:userId', getNotifications);
router.get('/:userId/unread', getUnreadNotifications);
router.patch('/:id/read', markAsRead);

module.exports = router;
