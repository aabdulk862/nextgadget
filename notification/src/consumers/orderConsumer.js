const { Kafka } = require('kafkajs');
const Notification = require('../models/Notification');

const brokers = (process.env.KAFKA_BROKERS || 'localhost:9092').split(',');

const kafka = new Kafka({
  clientId: 'notification-service',
  brokers
});

const consumer = kafka.consumer({ groupId: 'notification-group' });

const startConsumer = async () => {
  await consumer.connect();
  await consumer.subscribe({ topic: 'order-events', fromBeginning: false });

  await consumer.run({
    eachMessage: async ({ topic, partition, message }) => {
      try {
        const payload = JSON.parse(message.value.toString());
        const { orderId, userId, totalAmount, timestamp } = payload;

        await Notification.create({
          userId: String(userId),
          message: `Your order #${orderId} has been placed.`,
          status: 'unread',
          type: 'ORDER_EVENT'
        });

        console.log(`Notification created for order #${orderId}, user ${userId}`);
      } catch (err) {
        console.error('Error processing Kafka message:', err.message);
      }
    }
  });

  console.log('Kafka consumer started, listening on topic: order-events');
};

module.exports = { startConsumer };
