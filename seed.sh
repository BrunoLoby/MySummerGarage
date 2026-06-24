
set -euo pipefail

echo "Aguardando o Flyway criar as tabelas..."
until docker compose exec -T db psql -U postgres -d mysummergarage -tAc \
      "SELECT to_regclass('public.usuario')" 2>/dev/null | grep -q usuario; do
  sleep 2
done

echo "Tabelas prontas. Rodando seed..."
docker compose exec -T db psql -U postgres -d mysummergarage -v ON_ERROR_STOP=1 < seed-data.sql

echo "Seed concluído."
