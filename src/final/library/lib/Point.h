//
// Created by luiscarlos on 6/9/22.
//

#ifndef LIB_POINT_H
#define LIB_POINT_H

namespace PMath {
    class Point {
    public:
        Point(double x, double y) { m_x = x; m_y = y; }
        double getX() const { return m_x; }
        double getY() const { return m_y; }

    private:
        double m_x;
        double m_y;
    };
}

#endif //LIB_POINT_H
