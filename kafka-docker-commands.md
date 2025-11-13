# Kafka Docker Commands

## Prerequisites
- Docker and Docker Compose installed
- All services running: `docker-compose up -d`

## Topic Management

### Create a Topic
```bash
docker exec -it kafka1 kafka-topics \
  --bootstrap-server kafka1:29092 \
  --create \
  --topic twitter-tweets \
  --partitions 3 \
  --replication-factor 3 \
  --config min.insync.replicas=2
```

### List Topics
```bash
docker exec -it kafka1 kafka-topics \
  --bootstrap-server kafka1:29092 \
  --list
```

### Describe Topic
```bash
docker exec -it kafka1 kafka-topics \
  --bootstrap-server kafka1:29092 \
  --describe \
  --topic twitter-tweets
```

### Delete Topic
1. First, enable topic deletion in `docker-compose.yml`:
   ```yaml
   environment:
     KAFKA_DELETE_TOPIC_ENABLE: "true"
   ```
   Then restart the containers.

2. Delete the topic:
   ```bash
   docker exec -it kafka1 kafka-topics \
     --bootstrap-server kafka1:29092 \
     --delete \
     --topic twitter-tweets
   ```

## Cluster Management

### Check Cluster Health
```bash
docker exec -it kafka1 kafka-broker-api-versions \
  --bootstrap-server kafka1:29092
```

### Get Broker Information
```bash
docker exec -it kafka1 kafka-broker-api-versions \
  --bootstrap-server kafka1:29092
```

## Service Management

### Start Services
```bash
docker-compose up -d
```

### Stop Services
```bash
docker-compose down
```

### Stop and Remove Volumes (WARNING: Deletes all data)
```bash
docker-compose down -v
```

### View Logs
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f kafka1
```

## Common Issues

### If topics aren't being created
1. Check if all brokers are running:
   ```bash
   docker-compose ps
   ```

2. Check Kafka logs:
   ```bash
   docker-compose logs kafka1
   ```

### If getting connection refused
1. Ensure ZooKeeper is running:
   ```bash
   docker-compose logs zookeeper
   ```

2. Check if ports are available:
   ```bash
   lsof -i :9092,9093,9094
   ```

## Important Notes
- Always stop services with `docker-compose down` to prevent data corruption
- Data is persisted in Docker volumes
- Default ports:
  - kafka1: 9092
  - kafka2: 9093
  - kafka3: 9094
  - ZooKeeper: 2181
