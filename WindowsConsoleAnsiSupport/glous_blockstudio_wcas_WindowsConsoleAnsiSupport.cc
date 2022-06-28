#include "glous_blockstudio_wcas_WindowsConsoleAnsiSupport.h"
#include <windows.h>
JNIEXPORT jboolean JNICALL Java_glous_blockstudio_wcas_WindowsConsoleAnsiSupport_TryStart
        (JNIEnv *, jclass){
    HANDLE STDOUT;
    if ((STDOUT=GetStdHandle(STD_OUTPUT_HANDLE))!=INVALID_HANDLE_VALUE){
        DWORD ConsoleMode;
        if (GetConsoleMode(STDOUT,&ConsoleMode)){
            ConsoleMode=ConsoleMode|ENABLE_VIRTUAL_TERMINAL_PROCESSING;
            if (SetConsoleMode(STDOUT,ConsoleMode)){
                return true;
            }
        }
    }
    return false;
}