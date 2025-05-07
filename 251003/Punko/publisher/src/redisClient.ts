import IORedis from 'ioredis';

const redisHost = 'localhost';
const redisPort = parseInt('24120', 10);

console.log(`Connecting to Redis at ${redisHost}:${redisPort}`);

const redisClient = new IORedis({
    host: redisHost,
    port: redisPort,
    maxRetriesPerRequest: 3,
    enableReadyCheck: true,
    retryStrategy(times) {
        const delay = Math.min(times * 50, 2000);
        console.warn(`Redis connection attempt ${times} failed. Retrying in ${delay}ms...`);
        return delay;
    },
});

redisClient.on('connect', () => {
    console.log('Successfully connected to Redis.');
});

redisClient.on('ready', () => {
    console.log('Redis client is ready.');
});


redisClient.on('error', (err) => {
    console.error('Redis connection error:', err);
});

export default redisClient;
