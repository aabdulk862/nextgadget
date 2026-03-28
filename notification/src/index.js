require('dotenv').config();

const express = require('express');
const cors = require('cors');
const mongoose = require('mongoose');
const notificationRoutes = require('./routes/notificationRoutes');
const { startConsumer } = require('./consumers/orderConsumer');

const app = express();
const PORT = process.env.PORT || 8084;
const MONGO_URI = process.env.MONGO_URI || 'mongodb://localhost:27017/nextgadget_notifications';

app.use(cors());
app.use(express.json());

app.use('/api/notifications', notificationRoutes);

mongoose.connect(MONGO_URI)
  .then(async () => {
    console.log('Connected to MongoDB');

    try {
      await startConsumer();
    } catch (err) {
      console.error('Failed to start Kafka consumer:', err.message);
    }

    app.listen(PORT, () => {
      console.log(`Notification service running on port ${PORT}`);
    });
  })
  .catch((err) => {
    console.error('MongoDB connection failed:', err.message);
    process.exit(1);
  });
