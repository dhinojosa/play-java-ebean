# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table exercise (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  minutes                       integer,
  constraint pk_exercise primary key (id)
);


# --- !Downs

drop table if exists exercise;

