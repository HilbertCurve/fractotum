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
    fColor = vec4((v.x*v.x+v.y*v.y), 0.5f, 1 - (v.x*v.x+v.y*v.y), 1.0f);

    gl_Position = v;
}

#type fragment
#version 330 core
in vec4 fColor;

out vec4 color;

void main()
{
    color = vec4(fColor.rgb, 1.0f);
}
