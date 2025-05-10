import { cassandraClient } from '../db.js';
import { Reaction } from '../entities/reaction.entity.js';

export class ReactionRepository {
    async create(reaction: Reaction): Promise<Reaction> {
        const query = 'INSERT INTO tbl_reaction (id, content, issueid) VALUES (?, ?, ?)';
        const params = [reaction.id, reaction.content, reaction.issueid];
        console.log('Creating reaction:', params);
        await cassandraClient.execute(query, params, { prepare: true });
        return reaction;
    }

    async findAll(): Promise<Reaction[]> {
        const query = 'SELECT * FROM tbl_reaction';
        const result = await cassandraClient.execute(query);
        console.log('Fetched reactions:', result.rows);
        return result.rows.map(row => ({
            id: row.id,
            content: row.content,
            issueid: row.issueid,
        }));
    }

    async findById(id: number): Promise<Reaction | undefined> {
        console.log('Fetching reaction with ID:', id);
        const query = 'SELECT * FROM tbl_reaction WHERE id = ?';
        const result = await cassandraClient.execute(query, [id], { prepare: true });
        const row = result.first();
        console.log('Fetched reaction by ID:', row);
        return row ? { id: row.id, content: row.content, issueid: row.issueid } : undefined;
    }

    async update(reaction: Reaction): Promise<Reaction | undefined> {
        const query = 'UPDATE tbl_reaction SET content = ?, issueid = ? WHERE id = ?';
        const params = [reaction.content, reaction.issueid, reaction.id];
        console.log('Updating reaction:', params);
        await cassandraClient.execute(query, params, { prepare: true });
        return this.findById(reaction.id);
    }

    async delete(id: number): Promise<boolean> {
        try {
            console.log('Deleting reaction with ID:', id);
            const query = 'DELETE FROM tbl_reaction WHERE id = ?';
            const result = await cassandraClient.execute(query, [id], { prepare: true });
            console.log('Deleted reaction ID:', id);
            if(result.wasApplied()){
            return true}
            else{
            return false;}
        }
        catch (error){
            return false;
        }

    }
}