{
  sources ? import ./npins,
  pkgs ? import sources.nixpkgs {}
}:
pkgs.stdenv.mkDerivation (finalAttrs: {
  pname = "synced-lecterns";
  version = "v1.0-SNAPSHOT";
  src = pkgs.nix-gitignore.gitignoreSource [] ./.;

  nativeBuildInputs = with pkgs; [ gradle_9 openjdk25_headless ];

  mitmCache = pkgs.gradle_9.fetchDeps {
    pkg = finalAttrs.finalPackage;
    data = ./deps.json;
  };

  installPhase = ''
    cp build/libs/synced-lecterns.jar $out
  '';
})