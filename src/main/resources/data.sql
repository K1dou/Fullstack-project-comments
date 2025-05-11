TRUNCATE TABLE comment RESTART IDENTITY CASCADE;
TRUNCATE TABLE user_roles RESTART IDENTITY CASCADE;
TRUNCATE TABLE users RESTART IDENTITY CASCADE;





INSERT INTO users (nome, password, email, avatar_url) VALUES 
('juliusomo', '$2a$10$fSwS5oA5hSHoAB.AAEwwuuikEoewA0M22qHFlWks4Y3/8unsONZDa', 'julio@gmail.com', 'https://res.cloudinary.com/dyctkapys/image/upload/v1746382912/image-juliusomo_vfudnm.png'),
('amyrobson', '$2a$10$fSwS5oA5hSHoAB.AAEwwuuikEoewA0M22qHFlWks4Y3/8unsONZDa', 'amyrobson@gmail.com', 'https://res.cloudinary.com/dyctkapys/image/upload/v1746382979/image-amyrobson_wntnar.png'),
('maxblagun', '$2a$10$fSwS5oA5hSHoAB.AAEwwuuikEoewA0M22qHFlWks4Y3/8unsONZDa', 'maxblagun@gmail.com', 'https://res.cloudinary.com/dyctkapys/image/upload/v1746382958/image-maxblagun_ypfgiw.png'),
('ramsesmiron', '$2a$10$fSwS5oA5hSHoAB.AAEwwuuikEoewA0M22qHFlWks4Y3/8unsONZDa', 'ramsesmiron@gmail.com', 'https://res.cloudinary.com/dyctkapys/image/upload/v1746382968/image-ramsesmiron_k9pf7l.png'),
('Marcelo', '$2a$10$fSwS5oA5hSHoAB.AAEwwuuikEoewA0M22qHFlWks4Y3/8unsONZDa', 'hique1276@gmail.com', 'https://res.cloudinary.com/dyctkapys/image/upload/v1746379337/kirw1efs8asawxmfmctj.jpg');
INSERT INTO user_roles (user_id, role_id) VALUES 
(1, 1), -- USER
(2, 1), -- USER
(3, 1), -- USER
(4, 1), -- USER
(5, 1); -- USER

-- COMMENTS
-- Comentário principal (sem pai)
INSERT INTO comment (content, author_id, created_at) VALUES 
('Impressive! Though it seems the drag feature could be improved. But overall it looks incredible. You''ve nailed the design and the responsiveness at various breakpoints works really well.', 2, NOW());

INSERT INTO comment (content, author_id,created_at) VALUES 
('Woah, your project looks awesome! How long have you been coding for? I''m still new, but think I want to dive into React as well soon. Perhaps you can give me an insight on where I can learn React? Thanks!', 3, NOW());


INSERT INTO comment (content, author_id, parent_id, created_at) VALUES 
('@maxblagun if you`re still new, i''d recommend focusing on the fundamentals of HTML, CSS, and JS before considering Reac. it`s very tempting to jump ahead but lay a solid foundation first.', 4, 2, NOW());

INSERT INTO comment (content, author_id, parent_id, created_at) VALUES 
('@ramsesmiron i couldn`t agree more with this. Everything moves so fast and it always seems like everyone knows the newest library/framework. But the fundamentals are what stay constant.', 1, 2, NOW());
