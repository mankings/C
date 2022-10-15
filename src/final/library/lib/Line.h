//
// Created by luiscarlos on 5/30/22.
//

#ifndef PDRAW_LINE_H
#define PDRAW_LINE_H

#include <SDL_render.h>
#include "Color.h"
#include "Point.h"

namespace PMath {
    class Line {
    public:
        Line(Point p1, Point p2, int thickness, GFX::Color color);
        void draw(SDL_Renderer *renderer) const;

    private:
        Point m_p1 = Point(0, 0);
        Point m_p2 = Point(0, 0);
        int m_thickness;
        GFX::Color m_color = GFX::Color(0, 0, 0);
    };
}

#endif //PDRAW_LINE_H
