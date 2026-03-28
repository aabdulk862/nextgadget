# Notification Service

Node.js microservice that consumes Kafka order events and stores notifications in MongoDB. Part of the NextGadget e-commerce platform.

## Tech Stack

- **Runtime**: Node.js 20
- **Framework**: Express.js
- **Database**: MongoDB (via Mongoose)
- **Messaging**: Apache Kafka (via KafkaJS)

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/notifications/:userId` | Get all notifications for a user (sorted by date desc) |
| GET | `/api/notifications/:userId/unread` | Get unread notifications for a user |
| PATCH | `/api/notifications/:id/read` | Mark a notification as read |

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `PORT` | `8084` | HTTP server port |
| `MONGO_URI` | `mongodb://localhost:27017/nextgadget_notifications` | MongoDB connection string |
| `KAFKA_BROKERS` | `localhost:9092` | Comma-separated Kafka broker addresses |

## Kafka Consumer

Subscribes to the `order-events` topic (consumer group: `notification-group`). When an order event is received, a notification document is created with:

- `userId` — from the event payload
- `message` — `"Your order #<orderId> has been placed."`
- `status` — `"unread"`
- `type` — `"ORDER_EVENT"`

## Running Locally

```bash
npm install
npm start
```

Requires MongoDB and Kafka to be running. See the root `docker-compose.yml` for the full platform setup.

## Docker

```bash
docker build -t notification-service .
docker run -p 8084:8084 \
  -e MONGO_URI=mongodb://mongodb:27017/nextgadget_notifications \
  -e KAFKA_BROKERS=kafka:9092 \
  notification-service
```
