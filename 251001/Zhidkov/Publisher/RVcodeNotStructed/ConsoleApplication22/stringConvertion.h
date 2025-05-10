#pragma once
#include <string>
#include <windows.h>
//  Функции для преобразования UTF8 в ANSI
std::wstring utf8_to_wstring(const char* utf8_str) {
    int wlen = MultiByteToWideChar(CP_UTF8, 0, utf8_str, -1, NULL, 0);
    std::wstring wstr(wlen, 0);
    MultiByteToWideChar(CP_UTF8, 0, utf8_str, -1, &wstr[0], wlen);
    return wstr;
}

std::string wstring_to_ansi(const std::wstring& wstr, UINT codepage = 1251) {
    int len = WideCharToMultiByte(codepage, 0, wstr.c_str(), -1, NULL, 0, NULL, NULL);
    std::string str(len, 0);
    WideCharToMultiByte(codepage, 0, wstr.c_str(), -1, &str[0], len, NULL, NULL);
    return str;
}

std::string utf8_to_ansi(const char* utf8_str) {
    return wstring_to_ansi(utf8_to_wstring(utf8_str));
}