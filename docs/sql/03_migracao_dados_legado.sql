-- =============================================================================
-- 03_migracao_dados_legado.sql
-- Script de migracao e saneamento dos 10 registros de Plantas_Industriais.csv
-- =============================================================================

-- 1. Carga das Unidades Federativas presentes no legado
INSERT INTO unidades_federativas (sigla, nome, regiao) VALUES
  ('SP', 'São Paulo', 'Sudeste'),
  ('SC', 'Santa Catarina', 'Sul'),
  ('PR', 'Paraná', 'Sul'),
  ('RS', 'Rio Grande do Sul', 'Sul'),
  ('MG', 'Minas Gerais', 'Sudeste'),
  ('AM', 'Amazonas', 'Norte'),
  ('PE', 'Pernambuco', 'Nordeste');

-- 2. Migracao saneada das plantas (virgula->ponto, Sim/Nao->BOOLEAN, Enum normalizado, codigo_planta e status)
INSERT INTO plantas_industriais (codigo_planta, nome, cidade, uf_id, latitude, longitude, ano_inicio_operacao, area_m2, funcionarios, nivel_automacao, linhas_automatizadas, certificacao_iso9001, status) VALUES ('PLT-SP-001', 'Automax Industrial', 'São Bernardo do Campo', (SELECT id FROM unidades_federativas WHERE sigla = 'SP'), -23.69, -46.56, 2014, 57921, 500, 'MEDIO', 25, TRUE, 'ATIVA');
INSERT INTO plantas_industriais (codigo_planta, nome, cidade, uf_id, latitude, longitude, ano_inicio_operacao, area_m2, funcionarios, nivel_automacao, linhas_automatizadas, certificacao_iso9001, status) VALUES ('PLT-SP-002', 'Fábrica Digital ABC', 'São Paulo', (SELECT id FROM unidades_federativas WHERE sigla = 'SP'), -23.55, -46.63, 2012, 56435, 1653, 'ALTO', 16, FALSE, 'ATIVA');
INSERT INTO plantas_industriais (codigo_planta, nome, cidade, uf_id, latitude, longitude, ano_inicio_operacao, area_m2, funcionarios, nivel_automacao, linhas_automatizadas, certificacao_iso9001, status) VALUES ('PLT-SP-003', 'Indústria Conecta', 'Campinas', (SELECT id FROM unidades_federativas WHERE sigla = 'SP'), -22.91, -47.06, 2008, 46232, 1114, 'ALTO', 9, TRUE, 'ATIVA');
INSERT INTO plantas_industriais (codigo_planta, nome, cidade, uf_id, latitude, longitude, ano_inicio_operacao, area_m2, funcionarios, nivel_automacao, linhas_automatizadas, certificacao_iso9001, status) VALUES ('PLT-SC-004', 'NeoAutomação', 'Joinville', (SELECT id FROM unidades_federativas WHERE sigla = 'SC'), -26.30, -48.85, 2007, 54478, 1498, 'ALTO', 24, TRUE, 'ATIVA');
INSERT INTO plantas_industriais (codigo_planta, nome, cidade, uf_id, latitude, longitude, ano_inicio_operacao, area_m2, funcionarios, nivel_automacao, linhas_automatizadas, certificacao_iso9001, status) VALUES ('PLT-PR-005', 'SmartFactory Sul', 'Curitiba', (SELECT id FROM unidades_federativas WHERE sigla = 'PR'), -25.43, -49.27, 2004, 36483, 735, 'MEDIO', 10, TRUE, 'ATIVA');
INSERT INTO plantas_industriais (codigo_planta, nome, cidade, uf_id, latitude, longitude, ano_inicio_operacao, area_m2, funcionarios, nivel_automacao, linhas_automatizadas, certificacao_iso9001, status) VALUES ('PLT-RS-006', 'Robótica Vale', 'Caxias do Sul', (SELECT id FROM unidades_federativas WHERE sigla = 'RS'), -29.17, -51.18, 2018, 12631, 980, 'MEDIO', 4, TRUE, 'ATIVA');
INSERT INTO plantas_industriais (codigo_planta, nome, cidade, uf_id, latitude, longitude, ano_inicio_operacao, area_m2, funcionarios, nivel_automacao, linhas_automatizadas, certificacao_iso9001, status) VALUES ('PLT-MG-007', 'TecnoProcessos', 'Betim', (SELECT id FROM unidades_federativas WHERE sigla = 'MG'), -19.97, -44.20, 2005, 35528, 662, 'MEDIO', 5, TRUE, 'ATIVA');
INSERT INTO plantas_industriais (codigo_planta, nome, cidade, uf_id, latitude, longitude, ano_inicio_operacao, area_m2, funcionarios, nivel_automacao, linhas_automatizadas, certificacao_iso9001, status) VALUES ('PLT-SP-008', 'Linha 4.0 Brasil', 'Sorocaba', (SELECT id FROM unidades_federativas WHERE sigla = 'SP'), -23.50, -47.45, 2003, 20707, 2106, 'MUITO_ALTO', 7, TRUE, 'ATIVA');
INSERT INTO plantas_industriais (codigo_planta, nome, cidade, uf_id, latitude, longitude, ano_inicio_operacao, area_m2, funcionarios, nivel_automacao, linhas_automatizadas, certificacao_iso9001, status) VALUES ('PLT-AM-009', 'ControleMax', 'Manaus', (SELECT id FROM unidades_federativas WHERE sigla = 'AM'), -3.12, -60.02, 2004, 70174, 403, 'MUITO_ALTO', 21, FALSE, 'ATIVA');
INSERT INTO plantas_industriais (codigo_planta, nome, cidade, uf_id, latitude, longitude, ano_inicio_operacao, area_m2, funcionarios, nivel_automacao, linhas_automatizadas, certificacao_iso9001, status) VALUES ('PLT-PE-010', 'Sistemas Industriais Norte', 'Recife', (SELECT id FROM unidades_federativas WHERE sigla = 'PE'), -8.05, -34.88, 2014, 51686, 756, 'MUITO_ALTO', 17, FALSE, 'ATIVA');
