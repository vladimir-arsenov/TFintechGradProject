create table activity_request_user (
                                       activity_request_id bigint not null,
                                       user_id bigint not null,
                                       primary key (activity_request_id, user_id)
);