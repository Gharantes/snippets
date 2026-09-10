Para gerenciar os arquivos, vai precisar de um datasource.

Fiz com o Postgresql.

Pode criar um novo banco de dados só para isso, ou usar um existente.

De qualquer forma, colocar o nome do banco na url.

Formato: `jdbc:postgresql://localhost:5432/databricks_upload_dev`

Username e senha.

Nesse banco, vai precisar das seguintes tabelas:

```sql
create table sync_history (
	id serial4 primary key unique not null,
	at timestamp default current_timestamp not null
);
create table file_history (
	id serial4 primary key unique not null,
	added_at bigint references sync_history,
	last_validated_at bigint references sync_history,
	name varchar(255) not null
);
```