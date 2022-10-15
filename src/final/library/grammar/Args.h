#include <any>

namespace Inter
{
    enum Type {
        COLOR, THICKNESS, DIRECTION
    };

    struct Arg {
        Type t = Type::COLOR;
        std::any value = nullptr;
    };
} // namespace Inter
