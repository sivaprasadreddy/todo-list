create sequence todo_id_seq start with 1 increment by 1;

create table todo (
    id bigint DEFAULT nextval('todo_id_seq') not null,
    text varchar(1024) not null,
    created_on timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    done boolean,
    primary key (id)
);

