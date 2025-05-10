DELETE FROM public.tbl_post;
DELETE FROM public.tbl_issue;
DELETE FROM public.tbl_tag;

SELECT * FROM public.tbl_user
ORDER BY id ASC ;
DELETE FROM public.tbl_user WHERE id != 1;
ALTER TABLE tbl_user
	ALTER COLUMN id
		RESTART WITH 2;

ALTER TABLE tbl_issue
	ALTER COLUMN id
		RESTART WITH 1;


ALTER TABLE tbl_post
	ALTER COLUMN id
		RESTART WITH 1;

ALTER TABLE tbl_tag
	ALTER COLUMN id
		RESTART WITH 1;