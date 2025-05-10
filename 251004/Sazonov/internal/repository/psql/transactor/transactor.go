package transactor

import (
	"context"
	"database/sql"
	"fmt"

	"github.com/jmoiron/sqlx"
)

type txKey struct{}

type Transactor struct {
	db *sqlx.DB
}

func New(db *sqlx.DB) *Transactor {
	return &Transactor{
		db: db,
	}
}

func ExtractTx(ctx context.Context) *sqlx.Tx {
	if tx, ok := ctx.Value(txKey{}).(*sqlx.Tx); ok {
		return tx
	}

	return nil
}

func InjectTx(ctx context.Context, tx *sqlx.Tx) context.Context {
	return context.WithValue(ctx, txKey{}, tx)
}

func (t *Transactor) WithinTransaction(
	ctx context.Context,
	f func(txCtx context.Context) error,
	opts *sql.TxOptions,
) error {
	tx, err := t.db.BeginTxx(ctx, opts)
	if err != nil {
		return fmt.Errorf("within transaction: begin transaction: %w", err)
	}
	defer tx.Rollback()

	txCtx := InjectTx(ctx, tx)

	if err := f(txCtx); err != nil {
		return fmt.Errorf("within transaction: func: %w", err)
	}

	return nil
}
