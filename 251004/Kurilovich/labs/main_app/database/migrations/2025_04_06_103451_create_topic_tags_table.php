<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('tbl_topic_tag', function (Blueprint $table) {
            $table->foreignId('topic_id')
                ->references('id')
                ->on('tbl_topic')
                ->onDelete('cascade');
            $table->foreignId('tag_id')
                ->references('id')
                ->on('tbl_tag')
                ->onDelete('cascade');

            $table->primary(['topic_id', 'tag_id']);
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('topic_tags');
    }
};
