import redisClient from "../redisClient.js";

const DEFAULT_TTL_SECONDS = 60 * 10;

export class CacheService {

    async getOrSet<T>(
        key: string,
        fetchDataFn: () => Promise<T | null | undefined>,
        ttlSeconds: number = DEFAULT_TTL_SECONDS
    ): Promise<T | null | undefined> {
        try {
            const cachedData = await redisClient.get(key);
            if (cachedData) {
                try {
                    return JSON.parse(cachedData) as T;
                } catch (e) {
                    console.error(`Error parsing cached data for key ${key}:`, e);
                    await this.invalidate(key);
                }
            }
            console.log(`CACHE MISS for key: ${key}`);
            const freshData = await fetchDataFn();

            if (freshData !== null && freshData !== undefined) {
                await redisClient.set(key, JSON.stringify(freshData), 'EX', ttlSeconds);
                console.log(`CACHE SET for key: ${key} with TTL: ${ttlSeconds}s`);
            }
            return freshData;

        } catch (redisError) {
            console.error(`Redis error for key ${key}:`, redisError);
            console.warn(`Redis is unavailable, fetching fresh data directly for key: ${key}`);
            return await fetchDataFn();
        }
    }

    async invalidate(key: string): Promise<void> {
        try {
            const result = await redisClient.del(key);
            if (result > 0) {
                console.log(`CACHE INVALIDATED for key: ${key}`);
            }
        } catch (redisError) {
            console.error(`Redis error during invalidation for key ${key}:`, redisError);
        }
    }

    async invalidatePattern(pattern: string): Promise<void> {
        console.warn(`Attempting to invalidate pattern: ${pattern}. Use KEYS/SCAN with caution.`);
        try {
            let cursor = '0';
            do {
                const reply = await redisClient.scan(cursor, 'MATCH', pattern, 'COUNT', '100'); // Сканируем по 100 ключей за раз
                cursor = reply[0];
                const keys = reply[1];
                if (keys.length > 0) {
                    console.log(`Invalidating keys matching pattern ${pattern}:`, keys);
                    await redisClient.del(...keys);
                }
            } while (cursor !== '0');
            console.log(`Finished invalidating pattern: ${pattern}`);

        } catch (redisError) {
            console.error(`Redis error during pattern invalidation for ${pattern}:`, redisError);
        }
    }
}

export const cacheService = new CacheService();