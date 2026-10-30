CREATE TABLE learning_data (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    skill_name VARCHAR(255) NOT NULL,
    study_time INTEGER NOT NULL,
    study_month DATE NOT NULL,

    CONSTRAINT fk_learning_data_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_learning_data_category
        FOREIGN KEY (category_id)
        REFERENCES categories(id)
);