#!/bin/sh
# Licensed to the Apache Software Foundation (ASF)
# Licensed under the Apache License, Version 2.0

set -e

basedir=$(dirname "$(echo "$0" | sed -e 's,\\,/,g')")

# Use the maximum available, or set MAX_FD != maximum.
MAX_FD=maximum

warn() {
    echo "$*" >&2
}

die() {
    echo "$*" >&2
    exit 1
}

# OS specific support (must be 'true' or 'false').
cygwin=false
msys=false
darwin=false
nonstop=false

case "$(uname)" in
CYGWIN*) cygwin=true ;;
MINGW*) msys=true ;;
Darwin*) darwin=true ;;
NONSTOP*) nonstop=true ;;
esac

CLASSPATH=$basedir/.mvn/wrapper/maven-wrapper.jar

if [ -L "$0" ]; then
    LINK=$(readlink "$0")
    if expr "$LINK" : '/.*' >/dev/null; then
        basedir=$(dirname "$LINK")
    else
        basedir=$(dirname "$basedir"/"$LINK")
    fi
fi

cd "$basedir" || exit

if [ ! -e ".mvn/wrapper/maven-wrapper.jar" ]; then
    if command -v wget >/dev/null; then
        if [ "$MVNW_VERBOSE" = true ]; then
            echo "Couldn't find .mvn/wrapper/maven-wrapper.jar, downloading it ..."
        fi
        if [ -n "$MVNW_USERNAME" ] && [ -n "$MVNW_PASSWORD" ]; then
            mkdir -p "$basedir/.mvn/wrapper"
            echo "$MVNW_USERNAME:$MVNW_PASSWORD" | base64 | tr -d '\n' >"$basedir/.mvn/wrapper/maven-wrapper.password"
            wget --quiet \
                --auth-no-challenge \
                --password-file="$basedir/.mvn/wrapper/maven-wrapper.password" \
                -O "$basedir/.mvn/wrapper/maven-wrapper.jar" \
                "$MVNW_REPOURL/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
        else
            mkdir -p "$basedir/.mvn/wrapper"
            wget --quiet \
                -O "$basedir/.mvn/wrapper/maven-wrapper.jar" \
                "$MVNW_REPOURL/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
        fi
    elif command -v curl >/dev/null; then
        if [ "$MVNW_VERBOSE" = true ]; then
            echo "Couldn't find .mvn/wrapper/maven-wrapper.jar, downloading it ..."
        fi
        if [ -n "$MVNW_USERNAME" ] && [ -n "$MVNW_PASSWORD" ]; then
            mkdir -p "$basedir/.mvn/wrapper"
            echo "Basic $(printf '%s:%s' -n "$MVNW_USERNAME" "$MVNW_PASSWORD" | base64 | tr -d '\n')" >"$basedir/.mvn/wrapper/maven-wrapper.password"
            curl --silent \
                -H "$(cat "$basedir/.mvn/wrapper/maven-wrapper.password")" \
                -o "$basedir/.mvn/wrapper/maven-wrapper.jar" \
                "$MVNW_REPOURL/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
        else
            mkdir -p "$basedir/.mvn/wrapper"
            curl --silent \
                -o "$basedir/.mvn/wrapper/maven-wrapper.jar" \
                "$MVNW_REPOURL/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"
        fi
    else
        if [ "$MVNW_VERBOSE" = true ]; then
            echo "Falling back to using Java to download"
        fi
        jarsigner="$JAVA_HOME/bin/jarsigner"
        keytool="$JAVA_HOME/bin/keytool"
        mkdir -p "$basedir/.mvn/wrapper"
        if [ ! -s "$basedir/.mvn/wrapper/MavenWrapperDownloader.class" ]; then
            if [ "$MVNW_VERBOSE" = true ]; then
                echo " - Compiling MavenWrapperDownloader.java ..."
            fi
            ("$JAVA_HOME/bin/javac" \
                "$basedir/.mvn/wrapper/MavenWrapperDownloader.java" \
                -d "$basedir/.mvn/wrapper")
        fi
        if [ -s "$basedir/.mvn/wrapper/MavenWrapperDownloader.class" ]; then
            if [ "$MVNW_VERBOSE" = true ]; then
                echo " - Running MavenWrapperDownloader.java ..."
            fi
            ("$JAVA_HOME/bin/java" \
                -cp "$basedir/.mvn/wrapper" \
                MavenWrapperDownloader \
                "$MVNW_REPOURL/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar")
        fi
    fi
fi

export MAVEN_PROJECTBASEDIR=${MAVEN_BASEDIR:-"$basedir"}
if [ "$MVNW_VERBOSE" = true ]; then
    echo "$MAVEN_PROJECTBASEDIR"
fi
MAVEN_OPTS="$(concat_lines "$MAVEN_CONFIG") $MAVEN_OPTS"

EXECUTABLE="$JAVA_HOME/bin/java"

if [ -z "$JAVA_HOME" ]; then
    EXECUTABLE=java
    if ! command -v java >/dev/null 2>&1; then
        die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH."
    fi
fi

if [ -z "$JAVA_HOME" ]; then
    die "ERROR: this script requires JAVA_HOME to be set"
fi

if [ ! -x "$EXECUTABLE" ]; then
    die "ERROR: JAVA_HOME is not defined correctly for this system: $JAVA_HOME is not a recognized path to the Java executable."
fi

CLASSWORLDS_LAUNCHER=org.codehaus.plexus.classworlds.launcher.Launcher

# traverses directory structure from process work directory to filesystem root
# first directory with .mvn subdirectory is considered project base directory
find_maven_basedir() {
    if [ -z "$1" ]; then
        echo "ERROR: find_maven_basedir() received invalid filename or directory name: $1" >&2
        return 1
    else
        basedir=$(cd "$1" 2>/dev/null && pwd) || return 1
    fi

    while [ "$basedir" != '/' ]; do
        if [ -d "$basedir/.mvn" ]; then
            echo "$basedir"
            return 0
        fi
        basedir=$(dirname "$basedir")
    done
    return 1
}

concat_lines() {
    if [ -f "$1" ]; then
        ( cat "$1" | tr '\n' ' '; echo "" )
    fi
}

BASE_DIR=$(find_maven_basedir "$(pwd)")
if [ -z "$BASE_DIR" ]; then
    BASE_DIR=$(pwd)
fi

# concatenates all .mvn/jvm.config and .mvn/maven.config lines
MAVEN_CONFIG="$BASE_DIR/.mvn/jvm.config"
if [ -f "$MAVEN_CONFIG" ]; then
    MAVEN_CONFIG=$(concat_lines "$MAVEN_CONFIG")
fi

MAVEN_CONFIG="$BASE_DIR/.mvn/maven.config"
if [ -f "$MAVEN_CONFIG" ]; then
    MAVEN_CONFIG=$MAVEN_CONFIG" "$(concat_lines "$MAVEN_CONFIG")
fi

# Fork mode is not supported in msys, when using wildcards in arguments see http://sourceforge.net/tracker/?func=detail&aid=3202848&group_id=155316&atid=565529
if $cygwin; then
    CLASSPATH=$(
        tr -s : ' ' <<<"$CLASSPATH" | tr '\t' ' ' |
            sed 's/ /'"$(printf '\\n')"'/g'
    ) | sed 's/^/\/cygdrive\/c\//g' -e 's/$//' |
        tr '\n' ':'
    MAVEN_PROJECTBASEDIR=$(
        cygpath --path --mixed "$MAVEN_PROJECTBASEDIR"
    )
    CLASSPATH=$(
        cygpath --path --mixed "$CLASSPATH"
    )
    MAVEN_HOME=$(
        cygpath --path --mixed "$MAVEN_HOME"
    )
    JAVA_HOME=$(
        cygpath --path --mixed "$JAVA_HOME"
    )
    CLASSPATH=$CLASSPATH:$MAVEN_HOME/boot/plexus-classworlds-*.jar
    BOOT_CLASSPATH="-Xbootclasspath/a:$CLASSPATH"
    MAVEN_OPTS="$MAVEN_OPTS $BOOT_CLASSPATH"
    MAVEN_OPTS=$(
        cygpath --windows "$MAVEN_OPTS"
    )
else
    CLASSPATH=$CLASSPATH:$MAVEN_HOME/boot/plexus-classworlds-*.jar
    BOOT_CLASSPATH="-Xbootclasspath/a:$CLASSPATH"
    MAVEN_OPTS="$MAVEN_OPTS $BOOT_CLASSPATH"
fi

if [ "$MAVEN_VERBOSE" = true ]; then
    echo "$MAVEN_OPTS"
    echo "$CLASSPATH"
    echo ""
fi

# Provide a "standardized" way to retrieve the CLI args that will
# work with both Windows and non-Windows executions.
MAVEN_CMD_LINE_ARGS="$MAVEN_CONFIG $@"
export MAVEN_CMD_LINE_ARGS

WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

exec "$EXECUTABLE" \
    $MAVEN_OPTS \
    $MAVEN_VERBOSE \
    -classpath "$CLASSPATH" \
    "-Dmaven.multiModuleProjectDirectory=${MAVEN_PROJECTBASEDIR}" \
    ${WRAPPER_LAUNCHER} $MAVEN_CMD_LINE_ARGS
