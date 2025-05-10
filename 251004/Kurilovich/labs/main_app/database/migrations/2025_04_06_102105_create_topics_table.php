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
        Schema::create('tbl_topic', function (Blueprint $table) {
            $table->id()->primary();;
            $table->foreignId('editor_id')
                ->references('id')
                ->on('tbl_editor')
                ->onDelete('cascade');
            $table->string('title',64)->unique();
            $table->text('content');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('topics');
    }
};
