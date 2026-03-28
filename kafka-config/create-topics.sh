#!/bin/bash
# Create Kafka topics for NextGadget platform
# Usage: ./create-topics.sh [KAFKA_BROKER]
#
# This script creates the required Kafka topics for local development.
# It assumes a Kafka broker is running (default: localhost:9092).

KAFKA_BROKER="${1:-localhost:9092}"

echo "Creating Kafka topics on broker: $KAFKA_BROKER"

# Create the order-events topic
# - 3 partitions for parallel consumer processing
# - replication-factor 1 (suitable for local single-broker dev setup)
kafka-topics.sh \
  --bootstrap-server "$KAFKA_BROKER" \
  --create \
  --topic order-events \
  --partitions 3 \
  --replication-factor 1 \
  --if-not-exists

echo "Topic 'order-events' created (or already exists)."

# List all topics to verify
echo ""
echo "Current topics:"
kafka-topics.sh --bootstrap-server "$KAFKA_BROKER" --list
