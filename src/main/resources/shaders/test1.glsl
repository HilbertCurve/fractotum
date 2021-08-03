#type vertex
#version 330 core

precision mediump float;

layout (location = 0) in vec2 aPos;
layout (location = 1) in vec4 aColor;

out vec4 fColor;

uniform mat4 uView;
uniform mat4 uProjection;
uniform float uTime;

void main()
{
    vec4 v = uProjection * uView * vec4(aPos, 1.0, 1.0);
    fColor = vec4(abs(v.xy), 0.3, 1.0) * abs(sin(uTime)/2 + 0.5);

    gl_Position = v;
}

#type fragment
#version 330 core
in vec4 fColor;

out vec4 color;

void main()
{
    color = vec4(fColor);
}
