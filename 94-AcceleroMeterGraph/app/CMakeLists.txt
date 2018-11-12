

cmake_minimum_required(VERSION 3.4.1)


add_library( # Sets the name of the library.
             accelerometergraph

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/sensorgraph.cpp )



target_link_libraries(accelerometergraph
                      android
                      GLESv2
                      log)