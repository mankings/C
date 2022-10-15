//
// Created by luiscarlos on 5/30/22.
//

#ifndef PDRAW_COLOR_H
#define PDRAW_COLOR_H

namespace GFX {
    class Color {
    public:
        Color(int r, int g, int b);

        int getRed() const { return m_red; }
        int getBlue() const { return m_blue; }
        int getGreen() const { return m_green; }

    private:
        int m_red;
        int m_green;
        int m_blue;
    };
}

#endif //PDRAW_COLOR_H
