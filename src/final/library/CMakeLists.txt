# minimum required CMAKE version
CMAKE_MINIMUM_REQUIRED(VERSION 3.7 FATAL_ERROR)

list(APPEND CMAKE_MODULE_PATH ${CMAKE_CURRENT_SOURCE_DIR}/cmake)
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -pthread" )

# required if linking to static library
add_definitions(-DANTLR4CPP_STATIC)

# using /MD flag for antlr4_runtime (for Visual C++ compilers only)
set(ANTLR4_WITH_STATIC_CRT OFF)
# add external build for antlrcpp
include(ExternalAntlr4Cpp)
# add antrl4cpp artifacts to project environment
include_directories(${ANTLR4_INCLUDE_DIRS})

# set variable pointing to the antlr tool that supports C++
# this is not required if the jar file can be found under PATH environment
set(ANTLR_EXECUTABLE ${CMAKE_SOURCE_DIR}/dependencies/antlr-4.10.1-complete.jar)
# add macros to generate ANTLR Cpp code from grammar
find_package(ANTLR REQUIRED)
find_package(SDL2 REQUIRED)
find_package(SDL2_image REQUIRED)
find_package(Threads REQUIRED)

include_directories(${SDL2_INCLUDE_DIRS} dependencies/SDL2_image-2.0.5 dependencies/SDL2_GFX)

# Call macro to add lexer and grammar to your build dependencies.
antlr_target(PDrawPen Pen.g4 VISITOR)

# include generated files in project environment
include_directories(${ANTLR_PDrawPen_OUTPUT_DIR})

add_executable(PDraw ${ANTLR_PDrawPen_CXX_OUTPUTS} compilation.cpp lib/Pen.cpp lib/Pen.h lib/Direction.h lib/Constants.h lib/Line.cpp lib/Line.h lib/Color.cpp lib/Color.h grammar/Interpreter.cpp grammar/Interpreter.h dependencies/SDL2_GFX/SDL2_gfxPrimitives.h dependencies/SDL2_GFX/SDL2_gfxPrimitives.c dependencies/SDL2_GFX/SDL2_rotozoom.h dependencies/SDL2_GFX/SDL2_rotozoom.c lib/Point.h grammar/Args.h lib/Importer.h)
target_link_libraries(PDraw ${SDL2_LIBRARIES} ${SDL2_IMAGE_LIBRARIES} antlr4_static)
