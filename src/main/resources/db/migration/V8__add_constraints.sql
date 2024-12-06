alter table if exists activities
    add constraint activity_category_fk
        foreign key (category_id) references activity_categories;

alter table if exists activity_request_user
    add constraint activity_request_user_user_fk
        foreign key (user_id) references users;
alter table if exists activity_request_user
    add constraint activity_request_user_activity_request_fk
        foreign key (activity_request_id) references activity_requests;

alter table if exists activity_requests
    add constraint activity_request_activity_fk
        foreign key (activity_id) references activities;
alter table if exists activity_requests
    add constraint activity_request_user_fk
        foreign key (creator_id) references users;

alter table if exists jwt_tokens
    add constraint jwt_token_user_fk
        foreign key (user_id) references users;