BEGIN TRANSACTION;

-- Inserir 40 alunos fake
INSERT INTO [dbo].[tb_alunos] ([id], [cpf], [email], [nome]) VALUES
(NEWID(), '60800130022', 'ana.monteiro@example.com', 'Ana Monteiro'),
(NEWID(), '08798610023', 'bruno.nogueira@example.com', 'Bruno Nogueira'),
(NEWID(), '73788865059', 'carla.freitas@example.com', 'Carla Freitas'),
(NEWID(), '18693096001', 'daniel.goncalves@example.com', 'Daniel Gonçalves'),
(NEWID(), '69150143069', 'elisa.ramos@example.com', 'Elisa Ramos'),
(NEWID(), '66559655008', 'fabio.castro@example.com', 'Fábio Castro'),
(NEWID(), '69154928010', 'giovana.paz@example.com', 'Giovana Paz'),
(NEWID(), '75811208057', 'henrique.silva@example.com', 'Henrique Silva'),
(NEWID(), '62323758071', 'isabela.fernandes@example.com', 'Isabela Fernandes'),
(NEWID(), '93484993006', 'joao.medeiros@example.com', 'João Medeiros'),
(NEWID(), '62443053024', 'karina.torres@example.com', 'Karina Torres'),
(NEWID(), '32174100096', 'leandro.batista@example.com', 'Leandro Batista'),
(NEWID(), '87747490054', 'mariana.souza@example.com', 'Mariana Souza'),
(NEWID(), '35564244010', 'nicolas.teixeira@example.com', 'Nicolas Teixeira'),
(NEWID(), '90733388086', 'olivia.almeida@example.com', 'Olívia Almeida'),
(NEWID(), '72047571065', 'paulo.serra@example.com', 'Paulo Serra'),
(NEWID(), '18965431034', 'queila.lima@example.com', 'Queila Lima'),
(NEWID(), '49989799067', 'rafael.dias@example.com', 'Rafael Dias'),
(NEWID(), '72046568060', 'sabrina.cavalcanti@example.com', 'Sabrina Cavalcanti'),
(NEWID(), '67282196095', 'tulio.miranda@example.com', 'Túlio Miranda'),
(NEWID(), '93125216168', 'ana.claudia@example.com', 'Ana Cláudia'),
(NEWID(), '33734914876', 'bruno.lima@example.com', 'Bruno Lima'),
(NEWID(), '38130480204', 'camila.nogueira@example.com', 'Camila Nogueira'),
(NEWID(), '75173242337', 'daniel.souza@example.com', 'Daniel Souza'),
(NEWID(), '37530385470', 'elisa.martins@example.com', 'Elisa Martins'),
(NEWID(), '76087812547', 'fabio.almeida@example.com', 'Fábio Almeida'),
(NEWID(), '41502754592', 'gabriela.pires@example.com', 'Gabriela Pires'),
(NEWID(), '00624955770', 'henrique.azevedo@example.com', 'Henrique Azevedo'),
(NEWID(), '60436294613', 'isabela.silva@example.com', 'Isabela Silva'),
(NEWID(), '15077751137', 'joao.victor@example.com', 'João Victor'),
(NEWID(), '96625334278', 'karina.ferreira@example.com', 'Karina Ferreira'),
(NEWID(), '91935942832', 'lucas.ribeiro@example.com', 'Lucas Ribeiro'),
(NEWID(), '18654391774', 'mariana.castro@example.com', 'Mariana Castro'),
(NEWID(), '62264575891', 'nicolas.pereira@example.com', 'Nicolas Pereira'),
(NEWID(), '17488359712', 'olivia.fernandes@example.com', 'Olívia Fernandes'),
(NEWID(), '75939722245', 'pedro.machado@example.com', 'Pedro Machado'),
(NEWID(), '59875562360', 'quiteria.lopes@example.com', 'Quitéria Lopes'),
(NEWID(), '99729687749', 'rafael.gomes@example.com', 'Rafael Gomes'),
(NEWID(), '10781786746', 'sabrina.cardoso@example.com', 'Sabrina Cardoso'),
(NEWID(), '63002499056', 'tiago.rodrigues@example.com', 'Tiago Rodrigues');

-- Inserir 4 turmas
INSERT INTO [master].[dbo].[tb_turmas] ([id], [ano_letivo], [numero_turma]) VALUES
(NEWID(), 2025, 100),
(NEWID(), 2025, 125),
(NEWID(), 2025, 150),
(NEWID(), 2025, 200),
(NEWID(), 2025, 250),
(NEWID(), 2025, 300),
(NEWID(), 2025, 350),
(NEWID(), 2025, 400);

-- Vincular os alunos às turmas
WITH AlunosNumerados AS (
    SELECT 
        id AS aluno_id,
        ROW_NUMBER() OVER (ORDER BY id) AS rn
    FROM [master].[dbo].[tb_alunos]
),
TurmasNumeradas AS (
    SELECT 
        id AS turma_id,
        ROW_NUMBER() OVER (ORDER BY numero_turma) AS turma_num
    FROM [master].[dbo].[tb_turmas]
),
Relacionamento AS (
    SELECT 
        a.aluno_id,
        t.turma_id,
        a.rn
    FROM AlunosNumerados a
    JOIN TurmasNumeradas t 
        ON CEILING(a.rn * 1.0 / 5) = t.turma_num
)
-- Número máximo de alunos = número de turmas * 5
INSERT INTO [master].[dbo].[turmas_relationships_alunos] (aluno_id, turma_id)
SELECT aluno_id, turma_id 
FROM Relacionamento
WHERE rn <= (SELECT COUNT(*) FROM TurmasNumeradas) * 5;

-- Marcador para indicar o fim do script com sucesso
IF OBJECT_ID('SP_INIT_MARK', 'P') IS NULL
BEGIN
    EXEC('CREATE PROCEDURE SP_INIT_MARK AS BEGIN SELECT 1 END')
END;

COMMIT;
