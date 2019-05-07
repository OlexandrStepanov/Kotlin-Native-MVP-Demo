#!/bin/bash

set -e

# Setup
WORKSPACE="iosApp.xcworkspace"
SCHEME="Pods-iosApp"
CONFIGURATION="Debug"
PROJECT_DIR="$(pwd)"
OUTPUT_DIR="${PROJECT_DIR}/../common/src/iosMain/cinterop/bin/${CONFIGURATION}/"
BUILD_DIR="${PROJECT_DIR}/build"

rm -rf "${BUILD_DIR}"
rm -rf "${OUTPUT_DIR}"
mkdir -p "${OUTPUT_DIR}"


# Build the framework for device and for simulator (using all needed architectures).
xcodebuild -workspace "${WORKSPACE}" -scheme "${SCHEME}" -configuration ${CONFIGURATION} -arch x86_64 only_active_arch=no defines_module=yes -sdk "iphonesimulator" clean build CONFIGURATION_BUILD_DIR=${BUILD_DIR}/${CONFIGURATION}-iphonesimulator

xcodebuild -workspace "${WORKSPACE}" -scheme "${SCHEME}" -configuration ${CONFIGURATION} -arch arm64 only_active_arch=no defines_module=yes -sdk "iphoneos" clean build  CONFIGURATION_BUILD_DIR=${BUILD_DIR}/${CONFIGURATION}-iphoneos



for framework in $(find ${BUILD_DIR}/${CONFIGURATION}-iphoneos -name '*.framework'); do

  FRAMEWORK_PACKAGE=${framework##*/}
  FRAMEWORK_NAME=${FRAMEWORK_PACKAGE%.*}
    echo "Processing $framework; FRAMEWORK_PACKAGE=${FRAMEWORK_PACKAGE}; FRAMEWORK_NAME=${FRAMEWORK_NAME}"
  
  # Copy the device version of framework to output.
  cp -r "${framework}" "${OUTPUT_DIR}"

  # Replace the framework executable within the framework with a new version created by merging the device and simulator frameworks' executables with lipo.
  lipo -create -output "${OUTPUT_DIR}/${FRAMEWORK_PACKAGE}/${FRAMEWORK_NAME}" "${BUILD_DIR}/${CONFIGURATION}-iphoneos/${FRAMEWORK_PACKAGE}/${FRAMEWORK_NAME}" "${BUILD_DIR}/${CONFIGURATION}-iphonesimulator/${FRAMEWORK_PACKAGE}/${FRAMEWORK_NAME}"
  
done

# Delete build.
rm -rf "${BUILD_DIR}"
