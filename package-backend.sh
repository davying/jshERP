#!/bin/sh
set -eu

PROJECT_DIR=$(CDPATH= cd "$(dirname "$0")" && pwd)
BACKEND_DIR="$PROJECT_DIR/jshERP-boot"

IDEA_JDK_HOME="/Library/Java/JavaVirtualMachines/jdk1.8.0_351.jdk/Contents/Home"
IDEA_MAVEN_HOME="/Users/zhangdawei/project/apache-maven-3.3.9"
IDEA_MAVEN_SETTINGS="/Users/zhangdawei/project/apache-maven-3.3.9/conf/settings.xml"
IDEA_MAVEN_REPO="/Users/zhangdawei/project/repository"
MAVEN_BIN="$IDEA_MAVEN_HOME/bin/mvn"

if [ ! -x "$IDEA_JDK_HOME/bin/java" ]; then
  echo "JDK 不存在或不可执行: $IDEA_JDK_HOME" >&2
  exit 1
fi

if [ ! -x "$MAVEN_BIN" ]; then
  echo "Maven 不存在或不可执行: $MAVEN_BIN" >&2
  exit 1
fi

if [ ! -f "$IDEA_MAVEN_SETTINGS" ]; then
  echo "Maven settings.xml 不存在: $IDEA_MAVEN_SETTINGS" >&2
  exit 1
fi

if [ ! -f "$BACKEND_DIR/pom.xml" ]; then
  echo "后端 pom.xml 不存在: $BACKEND_DIR/pom.xml" >&2
  exit 1
fi

export JAVA_HOME="$IDEA_JDK_HOME"
export MAVEN_HOME="$IDEA_MAVEN_HOME"
export M2_HOME="$IDEA_MAVEN_HOME"
export PATH="$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH"

if [ -n "${MAVEN_OPTS:-}" ]; then
  export MAVEN_OPTS="$MAVEN_OPTS -Dfile.encoding=UTF-8"
else
  export MAVEN_OPTS="-Dfile.encoding=UTF-8"
fi

if [ "$#" -eq 0 ]; then
  set -- clean package
fi

echo "Project: $PROJECT_DIR"
echo "Backend: $BACKEND_DIR"
echo "JAVA_HOME: $JAVA_HOME"
echo "MAVEN_HOME: $MAVEN_HOME"
echo "Maven settings: $IDEA_MAVEN_SETTINGS"
echo "Maven local repo: $IDEA_MAVEN_REPO"
echo "Maven args: $*"
echo ""

"$JAVA_HOME/bin/java" -version
"$MAVEN_BIN" -v

cd "$BACKEND_DIR"
exec "$MAVEN_BIN" -s "$IDEA_MAVEN_SETTINGS" -Dmaven.repo.local="$IDEA_MAVEN_REPO" "$@"