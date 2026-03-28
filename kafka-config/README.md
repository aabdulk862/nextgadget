# Kafka Configuration

This directory contains scripts for setting up Apache Kafka topics used by the NextGadget platform.

## Prerequisites

- Apache Kafka broker running (default: `localhost:9092`)
- Zookeeper running (default: `localhost:2181`)
- Kafka CLI tools available on your PATH (included with Kafka installation)

If using Docker Compose, Kafka and Zookeeper are started automatically — see the root `docker-compose.yml`.

## Topics

| Topic | Description | Partitions | Replication Factor |
|-------|-------------|------------|--------------------|
| `order-events` | Order placement events published by the Order Service and consumed by the Notification Service | 3 | 1 |

## Usage

### Create topics manually

```bash
./create-topics.sh
```

To target a different broker:

```bash
./create-topics.sh kafka-broker:9092
```

### Create topics via Docker

If Kafka is running in Docker Compose:

```bash
docker exec -it <kafka-container> /opt/kafka/bin/kafka-topics.sh \
  --bootstrap-server localhost:9092 \
  --create --topic order-events \
  --partitions 3 --replication-factor 1 --if-not-exists
```

## Event Schema

### order-events

Published by the Order Service after a successful order placement.

```json
{
  "orderId": 1,
  "userId": 42,
  "totalAmount": 299.99,
  "timestamp": "2025-01-15T10:30:00"
}
```

The Notification Service consumes these events and creates user notifications in MongoDB.
