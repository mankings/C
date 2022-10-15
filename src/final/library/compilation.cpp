#include <vector>
#include <future>
#include <string>
#include "SDL.h"
#include "lib/Pen.h"
#include "lib/Color.h"
#include "lib/Point.h"
#include "lib/Importer.h"

std::vector<GFX::Pen*> pens;
std::mutex penMutex;

int render()
{
    if (SDL_Init(SDL_INIT_VIDEO) != 0) {
        return -1;
    }

    SDL_Window* window;
    SDL_Renderer* renderer;
    if (SDL_CreateWindowAndRenderer(1920, 1080, 0, &window, &renderer) != 0) {
        return -1;
    }

    SDL_bool done = SDL_FALSE;
    while (!done) {
        SDL_Event event;

        SDL_SetRenderDrawColor(renderer, 0, 0, 0, SDL_ALPHA_OPAQUE);
        SDL_RenderClear(renderer);
        {
            std::lock_guard<std::mutex> locker(penMutex);
            for (GFX::Pen *p : pens) {
                p->setDrawColor(renderer);
                for (PMath::Line l : p->getLines()) {
                    l.draw(renderer);
                }
            }
        }
        SDL_RenderPresent(renderer);

        while (SDL_PollEvent(&event)) {
            if (event.type == SDL_QUIT) {
                done = SDL_TRUE;
            }
        }
    }

    if (renderer) {
        SDL_DestroyRenderer(renderer);
    }
    if (window) {
        SDL_DestroyWindow(window);
    }

    return 0;
}

int main(int argc, char* argv[]) {
    std::thread renderThread(render);

    int var1 = 2;
    int var2 = var1;
    GFX::Color var3 = GFX::Color(255, 255, 255);
    GFX::Color var4 = var3;
    GFX::Pen* var5 = new GFX::Pen(GFX::PenArgs {.color = var4, .thickness = var2});
    pens.push_back(var5);
    int var7 = 0;
    int var8 = 0;
    PMath::Point var6(var7, var8);
    PMath::Point var9 = var6;
    var5->setPosition(var9);
    var5->down();
    double var10 = 270.0;
    var5->setAngle(var10);
    int var11 = 7;
    var5->moveForward(var11);
    double var12 = 0.0;
    var5->setAngle(var12);
    int var13 = 6;
    var5->moveForward(var13);
    double var14 = 90.0;
    var5->setAngle(var14);
    int var15 = 5;
    var5->moveForward(var15);
    double var16 = 180.0;
    var5->setAngle(var16);
    int var17 = 4;
    var5->moveForward(var17);
    double var18 = 270.0;
    var5->setAngle(var18);
    int var19 = 3;
    var5->moveForward(var19);
    double var20 = 0.0;
    var5->setAngle(var20);
    int var21 = 2;
    var5->moveForward(var21);
    double var22 = 90.0;
    var5->setAngle(var22);
    int var23 = 1;
    var5->moveForward(var23);
    var5->up();

    renderThread.join();
}