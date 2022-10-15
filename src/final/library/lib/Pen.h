//
// Created by luiscarlos on 5/30/22.
//

#ifndef PDRAW_PEN_H
#define PDRAW_PEN_H

#include <vector>
#include "Direction.h"
#include "Line.h"
#include "Color.h"
#include "Point.h"

namespace GFX {

    struct PenArgs {
    	double angle=0;
    	Color color=Color(255, 255, 255);
    	PMath::Point position=PMath::Point(0, 0);
    	int thickness=1;
    };

    class Pen {
    public:
        Pen();

        Pen(PenArgs args);

        void setPosition(PMath::Point newPosition);
        void setAngle(double angle);
        void moveForward(double distance);
        void up();
        void down();
        std::vector<PMath::Line> getLines();
        void setDrawColor(SDL_Renderer *renderer);
        void reassign(PenArgs newArgs);

    private:
        PMath::Point m_position = PMath::Point(0, 0);
        double m_angle;
        int m_thickness;
        bool m_isDown;
        std::vector<PMath::Line> m_linesDrawn;
        Color m_color = Color(0, 0, 0);
    };

} // GFX

#endif //PDRAW_PEN_H
