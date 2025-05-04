-- USERS
INSERT INTO users (username, password, email, avatar_url) VALUES 
('juliusomo', '123456', 'julio@gmail.com', 'https://res.cloudinary.com/dyctkapys/image/upload/v1746382912/image-juliusomo_vfudnm.png'),
('amyrobson', '123456', 'amyrobson@gmail.com', 'https://res.cloudinary.com/dyctkapys/image/upload/v1746382979/image-amyrobson_wntnar.png'),
('maxblagun', '123456', 'maxblagun@gmail.com', 'https://res.cloudinary.com/dyctkapys/image/upload/v1746382958/image-maxblagun_ypfgiw.png'),
('ramsesmiron', '123456', 'ramsesmiron@gmail.com', 'https://res.cloudinary.com/dyctkapys/image/upload/v1746382968/image-ramsesmiron_k9pf7l.png'),
('Marcelo', '123456', 'hique1276@gmail.com', 'https://res.cloudinary.com/dyctkapys/image/upload/v1746379337/kirw1efs8asawxmfmctj.jpg');

-- COMMENTS
-- Comentário principal (sem pai)
INSERT INTO comment (content, author_id, created_at) VALUES 
('Impressive! Though it seems the drag feature could be improved. But overall it looks incredible. You''ve nailed the design and the responsiveness at various breakpoints works really well.', 2, NOW());

-- Comentário com pai (resposta ao comentário acima)
INSERT INTO comment (content, author_id, parent_id, created_at) VALUES 
('Woah, your project looks awesome! How long have you been coding for? I''m still new, but think I want to dive into React as well soon. Perhaps you can give me an insight on where I can learn React? Thanks!', 3, 1, NOW());
