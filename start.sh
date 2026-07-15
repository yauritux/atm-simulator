#!/usr/bin/env sh
set -eu

ROOT_DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
cd "$ROOT_DIR"

mvn -q compile

exec java -cp "core/target/classes:cli-application/target/classes" com.dkatalis.AppRunner
