import { cassandraClient } from '../db.js';
import { Message } from '../entities/message.entity.js';

export class MessageRepository {
    async create(message: Message): Promise<Message> {
        const query = 'INSERT INTO tbl_message (id, content, articleid) VALUES (?, ?, ?)';
        const params = [message.id, message.content, message.articleid];
        console.log('Creating message:', params);
        await cassandraClient.execute(query, params, { prepare: true });
        return message;
    }

    async findAll(): Promise<Message[]> {
        const query = 'SELECT * FROM tbl_message';
        const result = await cassandraClient.execute(query);
        console.log('Fetched messages:', result.rows);
        return result.rows.map(row => ({
            id: row.id,
            content: row.content,
            articleid: row.articleid,
        }));
    }

    async findById(id: number): Promise<Message | undefined> {
        console.log('Fetching message with ID:', id);
        const query = 'SELECT * FROM tbl_message WHERE id = ?';
        const result = await cassandraClient.execute(query, [id], { prepare: true });
        const row = result.first();
        console.log('Fetched message by ID:', row);
        return row ? { id: row.id, content: row.content, articleid: row.articleid } : undefined;
    }

    async update(message: Message): Promise<Message | undefined> {
        const query = 'UPDATE tbl_message SET content = ?, articleid = ? WHERE id = ?';
        const params = [message.content, message.articleid, message.id];
        console.log('Updating message:', params);
        await cassandraClient.execute(query, params, { prepare: true });
        return this.findById(message.id);
    }

    async delete(id: number): Promise<boolean> {
        try {
            console.log('Deleting message with ID:', id);
            const query = 'DELETE FROM tbl_message WHERE id = ?';
            const result = await cassandraClient.execute(query, [id], { prepare: true });
            console.log('Deleted message ID:', id);
            return true
            //return result.rowLength !== null && result.rowLength !== undefined;
        }
        catch (error){
            return false;
        }

    }
}