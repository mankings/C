//
// Created by luiscarlos on 5/30/22.
//

#include "Line.h"
#include "SDL2_gfxPrimitives.h"

namespace PMath {
    Line::Line(Point p1, Point p2, int thickness, GFX::Color color) {
        m_p1 = p1;
        m_p2 = p2;
        m_thickness = thickness;
        m_color = color;
    }

    void Line::draw(SDL_Renderer *renderer) const {
        thickLineRGBA(renderer, (Sint16) m_p1.getX(), (Sint16) m_p1.getY(), (Sint16) m_p2.getX(), (Sint16) m_p2.getY(), m_thickness, m_color.getRed(), m_color.getGreen(), m_color.getBlue(), 255);
    }
}
