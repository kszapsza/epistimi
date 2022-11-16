#!/bin/bash

while getopts m:n: flag; do
  case "${flag}" in
  m) module=${OPTARG} ;;
  n) component_name=${OPTARG} ;;
  *) ;;
  esac
done

if [ "$module" = "" ] || [ "$component_name" = "" ]; then
  echo "Usage: create-component -m <module_name> -n <component_name>"
  exit
fi

mkdir "./src/components/$module/$component_name"
cd "./src/components/$module/$component_name" || exit

touch "$component_name.tsx"
echo "\
import './${component_name}.scss';

interface ${component_name}Props {
}

export const ${component_name} = (
  {}: ${component_name}Props,
): JSX.Element => {
  return (
    <></>
  );
};
" >> "$component_name.tsx"
git add "$component_name.tsx"

touch "$component_name.scss"
git add "$component_name.scss"

touch "index.ts"
echo "\
export { $component_name } from './$component_name';
" >> "index.ts"
git add "index.ts"

cd ..
echo -e "\nexport { $component_name } from './$component_name';" >> "index.ts"

echo "Created new React component $component_name under ./src/components/$module/$component_name!"
