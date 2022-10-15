//
// Created by luiscarlos on 5/30/22.
//

#include <cmath>
#include <iostream>
#include "Pen.h"
#include "Constants.h"

namespace GFX {
    Pen::Pen(PenArgs args) {
        m_position = args.position;
        m_angle = deg2rad * args.angle;
        m_isDown = false;
        m_color = args.color;
        m_thickness = args.thickness;
    }

    void Pen::setPosition(PMath::Point newPosition) {
        if (m_isDown) {
            m_linesDrawn.emplace_back(m_position, newPosition, m_thickness, m_color);
        }
        m_position = newPosition;
    }

    void Pen::setAngle(double angle) {
        m_angle += (double) deg2rad * angle;
    }

    void Pen::moveForward(double distance) {
        double c1 = distance * cos(m_angle);
        double c2 = distance * sin(m_angle);

        if (m_isDown) {
            m_linesDrawn.emplace_back(m_position, PMath::Point(m_position.getX() + c1, m_position.getY() + c2), m_thickness, m_color);
        }

        m_position = PMath::Point(m_position.getX() + c1, m_position.getY() + c2);
    }

    void Pen::up() {
        m_isDown = false;
    }

    void Pen::down() {
        m_isDown = true;
    }

    std::vector<PMath::Line> Pen::getLines() {
        return m_linesDrawn;
    }

    void Pen::setDrawColor(SDL_Renderer *renderer) {
        SDL_SetRenderDrawColor(renderer, m_color.getRed(), m_color.getGreen(), m_color.getBlue(), SDL_ALPHA_OPAQUE);
    }

    Pen::Pen() {
        m_position = PMath::Point(0, 0);
        m_angle = 0;
        m_isDown = false;
        m_color = Color(255, 255, 255);
        m_thickness = 1;
    }

    void Pen::reassign(PenArgs newArgs) {
        auto defaults = PenArgs {};
        if ((defaults.position.getX() != newArgs.position.getX() || defaults.position.getY() != newArgs.position.getY()) && (m_position.getX() != newArgs.position.getX() || m_position.getY() != newArgs.position.getY())) {
            m_position = newArgs.position;
        }

        if ((defaults.angle != newArgs.angle) && (newArgs.angle != m_angle)) {
            m_angle = newArgs.angle;
        }

        if ((defaults.thickness != newArgs.thickness) && (newArgs.thickness != m_thickness)) {
            m_angle = newArgs.angle;
        }

        if ((defaults.color.getRed() != newArgs.color.getRed() || defaults.color.getBlue() != newArgs.color.getBlue() || defaults.color.getGreen() != newArgs.color.getGreen()) && (m_color.getRed() != newArgs.color.getRed() || m_color.getBlue() != newArgs.color.getBlue() || m_color.getGreen() != newArgs.color.getGreen())) {
            m_position = newArgs.position;
        }
    }
} // GFX
