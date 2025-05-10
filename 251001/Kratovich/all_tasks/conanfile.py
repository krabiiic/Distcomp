from conan import ConanFile
from conan.tools.cmake import cmake_layout, CMake


class ExampleRecipe(ConanFile):
    settings = "os", "compiler", "build_type", "arch"
    generators = "CMakeDeps", "CMakeToolchain"

    def requirements(self):
        self.requires("cpp-httplib/0.18.3")
        self.requires("nlohmann_json/3.11.3")
        self.requires("libpqxx/7.10.0")
        self.requires("cassandra-cpp-driver/2.17.1")
        self.requires("librdkafka/2.8.0")
        self.requires("redis-plus-plus/1.3.13")

    def layout(self):
        cmake_layout(self)

    def build(self):
        cmake = CMake(self)
        cmake.configure()
        cmake.build()