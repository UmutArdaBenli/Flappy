#type vertex
#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aBlockPos; // this will actually receive color attribute

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec2 TexCoord;

void main()
{
    vec4 worldPos = model * vec4(aPos, 1.0);
    gl_Position = projection * view * worldPos;

    // UV coordinates are based on block's world position now
    TexCoord.x = aBlockPos.x - floor(aBlockPos.x);
    TexCoord.y = aBlockPos.z - floor(aBlockPos.z); // assuming blocks are positioned along x-z plane
}

#type fragment
#version 330 core
out vec4 FragColor;
in vec2 TexCoord;
uniform sampler2D ourTexture;


void main()
{
    FragColor = texture(ourTexture, TexCoord);
}