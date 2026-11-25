-- =============================================================================
-- 01_modelo_legado.sql
-- Reconstrucao fiel da estrutura tabular original (Plantas_Industriais.csv)
-- =============================================================================
CREATE TABLE plantas_industriais_legado (
    id_planta            INT,
    planta               VARCHAR(255),
    cidade               VARCHAR(255),
    estado               VARCHAR(20),     -- Texto livre sem FK ou dominio controlado
    latitude             VARCHAR(30),     -- Armazenado como texto com virgula ("-23,69")
    longitude            VARCHAR(30),     -- Armazenado como texto com virgula ("-46,56")
    ano_inicio_operacao  INT,
    area_m2              INT,
    funcionarios         INT,
    nivel_automacao      VARCHAR(50),     -- Texto livre com inconsistencia ("Muito alto")
    linhas_automatizadas INT,
    certificacao_iso9001 VARCHAR(10)      -- Booleano armazenado como "Sim"/"Não"
);
