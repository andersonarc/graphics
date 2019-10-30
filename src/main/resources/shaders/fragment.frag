#version 450

in vec2 outTexCoord;
in vec3 mvVertexNormal;
in vec3 mvVertexPos;
out vec4 fragColor;

struct Attenuation {
    float constant;
    float linear;
    float exponent;
};

struct PointLight {
    vec3 colour;
    vec3 position;
    float intensity;
    Attenuation att;
};

struct Material {
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    int hasTexture;
    float reflectance;
};

uniform sampler2D textureSampler;

uniform vec3 ambientLight;
uniform float specularPower;
uniform Material material;
uniform PointLight pointLight;

vec4 ambientC;
vec4 diffuseC;
vec4 speculrC;

void setupColours(Material material, vec2 textCoord) {
    if (material.hasTexture == 1) {
        ambientC = texture(textureSampler, textCoord);
        diffuseC = ambientC;
        speculrC = ambientC;
    } else {
        ambientC = material.ambient;
        diffuseC = material.diffuse;
        speculrC = material.specular;
    }
}

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal) {
    vec4 diffuseColour = vec4(0, 0, 0, 0);
    vec4 specColour = vec4(0, 0, 0, 0);

    // Diffuse Light
    vec3 lightDirection = light.position - position;
    vec3 toLightSource  = normalize(lightDirection);
    float diffuseFactor = max(dot(normal, toLightSource), 0.0);
    diffuseColour = diffuseC * vec4(light.colour, 1.0) * light.intensity * diffuseFactor;

    // Specular Light
    vec3 cameraDirection = normalize(-position);
    vec3 fromLightSource = -toLightSource;
    vec3 reflectedLight = normalize(reflect(fromLightSource, normal));
    float specularFactor = max(dot(cameraDirection, reflectedLight), 0.0);
    specularFactor = pow(specularFactor, specularPower);
    specColour = speculrC * specularFactor * material.reflectance * vec4(light.colour, 1.0);

    // Attenuation
    float distance = length(lightDirection);
    float attenuationInv = light.att.constant + light.att.linear * distance +
    light.att.exponent * distance * distance;
    return (diffuseColour + specColour) / attenuationInv;
}

void main() {
    setupColours(material, outTexCoord);
    vec4 diffuseSpecularComp = calcPointLight(pointLight, mvVertexPos, mvVertexNormal);
    fragColor = ambientC * vec4(ambientLight, 1) + diffuseSpecularComp;
}